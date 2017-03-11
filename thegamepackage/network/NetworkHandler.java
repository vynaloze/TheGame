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
import java.util.Properties;


public class NetworkHandler implements PlayerHandlerInterface {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameMessage currentMove, currentAttack, currentSkill, currentRotation, isTurnOver;
    private boolean isServer;

    public NetworkHandler(TheGame theGame, boolean isServer, String ip, int port) {
        this.isServer = isServer;
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

    private void readMessageFromSocket() {
        GameMessage currentMessage = null;
        try {
            currentMessage = (GameMessage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (currentMessage == null) {
            return;
        }
        switch (currentMessage.type) {
            case ENDTURN:
                isTurnOver = new GameMessage();
                isTurnOver = currentMessage;
                break;
            case ATTACK:
                currentAttack = currentMessage;
                break;
            case MOVE:
                currentMove = new GameMessage();
                currentMove = currentMessage;
                break;
            case ROTATION:
                currentRotation = currentMessage;
                break;
            case SKILL:
                currentSkill = currentMessage;
                break;
        }
    }


    /*public void processOutput(GameMessage message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @Override
    public GameMessage getMove() {
          readMessageFromSocket();
        //take it from the socket
        GameMessage message = currentMove;
        currentMove = null;
        return message;
    }

    @Override
    public void performedMove(GameMessage move) {
        //send it to the socket
        try {
            out.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getAttack() {
        GameMessage message = currentAttack;
        currentAttack = null;
        return message;
    }

    @Override
    public void performedAttack(GameMessage position) {
        try{
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getSkill() {
        GameMessage message = currentSkill;
        currentSkill = null;
        return message;
    }

    @Override
    public void performedSkill(GameMessage position) {
        try{
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage getRotation() {
        GameMessage message = currentRotation;
        currentRotation = null;
        return message;
    }

    @Override
    public void performedRotation(GameMessage position) {
        try{
            out.writeObject(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMessage isTurnOver() {
 //todo: check       readMessageFromSocket();
        GameMessage message = isTurnOver;
        isTurnOver = null;
        return message;
    }

    @Override
    public void confirmEndTurn() {
        GameMessage message = new GameMessage();
        message.type = GameMessage.TypeOfMessage.ENDTURN;
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

    public void sendGameConditions(GameConditions message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*private class handleInputs implements Runnable {
        ObjectInputStream in;
        ActionsProtocol ap;

        private handleInputs(ObjectInputStream in) {
            this.in = in;
            this.ap = new ActionsProtocol();
        }

        @Override
        public void run() {
            try {
                GameMessage m = (GameMessage) in.readObject();
                ap.processData(m);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }*/

    public boolean isServer() {
        return isServer;
    }
}
