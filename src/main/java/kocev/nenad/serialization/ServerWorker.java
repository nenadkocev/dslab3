package main.java.kocev.nenad.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ServerWorker implements Runnable {

    private Socket socket;
    private Application application;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;

    public ServerWorker(Socket socket, Application application) {
        this.socket = socket;
        this.application = application;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            ous = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStreams() {
        try {
            ous.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            MessageType messageType = (MessageType) ois.readObject();
            if(messageType == MessageType.IWantAJobDone){
                User user = (User) ois.readObject();
                Task task = (Task) ois.readObject();

                application.insertNewJob(user, task);
            }
            else if(messageType == MessageType.IWantAJob){
                Optional<Task> task = application.getUndoneTask();
                if(task.isEmpty()){
                    ous.writeObject(MessageType.JobNotFound);
                }
                else {
                    ous.writeObject(MessageType.NewJobFound);
                    ous.writeObject(task.get());
                }
            }
            else if(messageType == MessageType.IHaveDoneAJob){
                Task task = (Task) ois.readObject();
                Message message;
                if(task.isDone()){
                    application.doneTask(task);
                    message = new Message("GREAT, HERES UAN TAUSAND DOLLARS");
                }
                else {
                    message = new Message("NO COOKIES FOR U");
                }
                ous.writeObject(message);
            }
            else if(messageType == MessageType.IsMyJobDone){
                User user = (User) ois.readObject();
                List<Task> doneTasksForUser = application.getDoneTasksForUser(user);
                ous.writeObject(doneTasksForUser);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            closeStreams();
        }
    }
}
