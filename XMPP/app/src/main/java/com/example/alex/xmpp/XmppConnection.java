package com.example.alex.xmpp;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;

import java.io.IOException;

public class XmppConnection {
    public final static int SERVER_PORT = 5222;//服务端口 可以在openfire上设置
    public  final static String SERVER_HOST = "220.134.248.18";//你openfire服务器所在的ip
    public final static String SERVER_NAME = "Android";//设置openfire时的服务器名
    private static XMPPConnection connection = null;

    private static void openConnection() {
        try {
            if (null == connection || !connection.isAuthenticated()) {
                //配置连接
                try {
                    ConnectionConfiguration config = new ConnectionConfiguration(
                            SERVER_HOST, SERVER_PORT,
                            SERVER_NAME);
                    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);//明文傳輸
                    config.setDebuggerEnabled(true);//調適模式
                    connection = new XMPPConnection(config) {
                        @Override
                        public String getUser() {
                            return null;
                        }

                        @Override
                        public String getConnectionID() {
                            return null;
                        }

                        @Override
                        public boolean isConnected() {
                            return false;
                        }

                        @Override
                        public boolean isAuthenticated() {
                            return false;
                        }

                        @Override
                        public boolean isAnonymous() {
                            return false;
                        }

                        @Override
                        public boolean isSecureConnection() {
                            return false;
                        }

                        @Override
                        protected void sendPacketInternal(Packet packet) throws SmackException.NotConnectedException {

                        }

                        @Override
                        public boolean isUsingCompression() {
                            return false;
                        }

                        @Override
                        protected void connectInternal() throws SmackException, IOException, XMPPException {

                        }

                        @Override
                        public void login(String s, String s1, String s2) throws XMPPException, SmackException, SaslException, IOException {

                        }

                        @Override
                        public void loginAnonymously() throws XMPPException, SmackException, SaslException, IOException {

                        }

                        @Override
                        protected void shutdown() {

                        }
                    };
                    connection.connect();//连接到服务器
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //配置各种Provider，如果不配置，则会无法解析数据
                //   configureConnection(ProviderManager.getInstance());
            }
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }

    /**
     * 创建连接
     */
    public static XMPPConnection getConnection() {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        try {
            connection.disconnect();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        connection = null;
    }
    /**
     * xmpp配置
     */
    /*
    private static void configureConnection(ProviderManager pm) {
        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",new GroupChatInvitation.Provider());
        // Service Discovery # Items //解析房间列表
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
        // Service Discovery # Info //某一个房间的信息
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
        pm.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());
    }
    */
}
