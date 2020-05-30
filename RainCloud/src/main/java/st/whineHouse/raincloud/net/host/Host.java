package st.whineHouse.raincloud.net.host;

public abstract class Host {
    public abstract void send(byte[] data) throws Exception;
    public abstract void broadcastToClients(byte[] data) throws Exception;
}
