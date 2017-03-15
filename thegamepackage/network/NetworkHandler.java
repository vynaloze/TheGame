package thegamepackage.network;

import thegamepackage.logic.GameConditions;
import thegamepackage.logic.PlayerHandlerInterface;
import thegamepackage.logic.TheGame;
import thegamepackage.util.GameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class NetworkHandler implements PlayerHandlerInterface {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameMessage lastMessageReceived;

    //------------------------------------------
    // everything with opening and closing connection
    public NetworkHandler(TheGame theGame, boolean isServer, String ip, int port) {
        if (isServer) {
            setServerConnection(port);
        } else {
            setClientConnection(ip, port);
        }
    }

    public void closeConnection() {
        try {
            out.close();
            in.close();
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setServerConnection(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setClientConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //------------------------------------------
    // methods for I/O of any in-game message
    @Override
    public GameMessage getMove() {
        // take message from socket, once is enough for the whole cycle
        try {
            lastMessageReceived = (GameMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.MOVE) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void performedMove(GameMessage move) {
        //send it to the socket if it is not the one just received
        if (lastMessageReceived == move) {
            return;
        }
        try {
            out.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getAttack() {
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.ATTACK) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void performedAttack(GameMessage position) {
        if (lastMessageReceived == position) {
            return;
        }
        try {
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getSkill() {
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.SKILL) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void performedSkill(GameMessage position, String player) {
        if (lastMessageReceived == position) {
            return;
        }
        try {
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getRotation() {
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.ROTATION) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void performedRotation(GameMessage position) {
        if (lastMessageReceived == position) {
            return;
        }
        try {
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage isTurnOver() {
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.ENDTURN) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void confirmEndTurn(GameMessage message) {
        if (lastMessageReceived == message) {
            return;
        }
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getChatMessage() {
        if (lastMessageReceived.type == GameMessage.TypeOfMessage.CHAT) {
            return lastMessageReceived;
        }
        return null;
    }

    @Override
    public void sendChatMessage(GameMessage message, String player) {
        if (lastMessageReceived == message) {
            return;
        }
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameConditions getGameConditions() {
        GameConditions message = null;
        try {
            message = (GameConditions) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void sendGameConditions(GameConditions message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
