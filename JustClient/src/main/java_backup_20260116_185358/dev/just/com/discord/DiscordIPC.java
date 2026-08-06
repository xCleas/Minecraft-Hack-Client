package dev.just.com.discord;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class DiscordIPC {
    private static final Gson GSON = new Gson();
    private RandomAccessFile pipe;
    private boolean connected = false;
    private final long clientId;

    public DiscordIPC(long clientId) {
        this.clientId = clientId;
    }

    public boolean connect() {
        for (int i = 0; i < 10; i++) {
            try {
                String pipePath = "\\\\.\\pipe\\discord-ipc-" + i;
                this.pipe = new RandomAccessFile(pipePath, "rw");
                this.connected = true;

                // Handshake
                JsonObject handshake = new JsonObject();
                handshake.addProperty("v", 1);
                handshake.addProperty("client_id", String.valueOf(clientId));
                send(0, GSON.toJson(handshake));

                // Read response
                readResponse();
                return true;
            } catch (Exception e) {
                // Try next pipe
            }
        }
        return false;
    }

    public void updatePresence(String details, String state, String largeImageKey,
                               String largeImageText, long startTimestamp,
                               String buttonLabel, String buttonUrl) {
        if (!connected || pipe == null) return;

        try {
            JsonObject activity = new JsonObject();

            if (details != null) activity.addProperty("details", details);
            if (state != null) activity.addProperty("state", state);

            // Timestamps
            if (startTimestamp > 0) {
                JsonObject timestamps = new JsonObject();
                timestamps.addProperty("start", startTimestamp);
                activity.add("timestamps", timestamps);
            }

            // Assets (images)
            JsonObject assets = new JsonObject();
            if (largeImageKey != null) {
                assets.addProperty("large_image", largeImageKey);
                if (largeImageText != null) {
                    assets.addProperty("large_text", largeImageText);
                }
            }
            if (assets.size() > 0) {
                activity.add("assets", assets);
            }

            // Buttons
            if (buttonLabel != null && buttonUrl != null) {
                com.google.gson.JsonArray buttons = new com.google.gson.JsonArray();
                JsonObject button1 = new JsonObject();
                button1.addProperty("label", buttonLabel);
                button1.addProperty("url", buttonUrl);
                buttons.add(button1);
                activity.add("buttons", buttons);
            }

            // Build command
            JsonObject args = new JsonObject();
            args.addProperty("pid", ProcessHandle.current().pid());
            args.add("activity", activity);

            JsonObject cmd = new JsonObject();
            cmd.addProperty("cmd", "SET_ACTIVITY");
            cmd.add("args", args);
            cmd.addProperty("nonce", UUID.randomUUID().toString());

            send(1, GSON.toJson(cmd));
            readResponse();
        } catch (Exception e) {
            // Ignore errors
        }
    }

    private void send(int opcode, String data) throws IOException {
        byte[] bytes = data.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(8 + bytes.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(opcode);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        pipe.write(buffer.array());
    }

    private String readResponse() {
        try {
            byte[] header = new byte[8];
            pipe.readFully(header);
            ByteBuffer headerBuffer = ByteBuffer.wrap(header);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int opcode = headerBuffer.getInt();
            int length = headerBuffer.getInt();

            byte[] data = new byte[length];
            pipe.readFully(data);
            return new String(data, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public void disconnect() {
        connected = false;
        if (pipe != null) {
            try {
                pipe.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
