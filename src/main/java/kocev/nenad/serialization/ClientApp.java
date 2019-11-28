package main.java.kocev.nenad.serialization;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

public class ClientApp {
    private static Random random = new Random();

    public void simulateIWantAJobDone() throws IOException {
        Socket socket = new Socket(Server.SERVER_ADDRESS, Server.SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);
        User user = new User(UUID.randomUUID().toString(), User.UserRole.CLIENT);
        Task task = new Task(user, random.nextDouble(), random.nextDouble(), Task.TaskOperator.SUBTRACTION);
        ous.writeObject(MessageType.IWantAJobDone);
        ous.writeObject(user);
        ous.writeObject(task);
        ois.close();
        ous.close();
    }

    public void simulateIWantAJob() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(Server.SERVER_ADDRESS, Server.SERVER_PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        ous.writeObject(MessageType.IWantAJob);
        MessageType messageType = (MessageType) ois.readObject();
        if(messageType == MessageType.JobNotFound){
            System.out.println("But I know java...");
            ous.close();
            ois.close();
        }
        else {
            Task taskToBeDone = (Task) ois.readObject();
            ous.close();
            ois.close();
            simulateIHaveDoneAJob(taskToBeDone);
        }

    }

    private void simulateIHaveDoneAJob(Task taskToBeDone) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(Server.SERVER_ADDRESS, Server.SERVER_PORT);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        ObjectOutputStream ous = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        ous.writeObject(MessageType.IHaveDoneAJob);
        boolean amIGood = random.nextBoolean();
        if(amIGood){
            taskToBeDone.setDone(true);
            Task.TaskOperator operator = taskToBeDone.getOperator();
            switch (operator){
                case SUBTRACTION: taskToBeDone.setResult(taskToBeDone.getOperand1() - taskToBeDone.getOperand2());
                break;
                case ADDITION: taskToBeDone.setResult(taskToBeDone.getOperand1() + taskToBeDone.getOperand2());
                break;
                case DIVISION: taskToBeDone.setResult(taskToBeDone.getOperand1() / taskToBeDone.getOperand2());
                break;
                case MULTIPLICATION: taskToBeDone.setResult(taskToBeDone.getOperand1() * taskToBeDone.getOperand2());
                break;
            }
            System.out.println("I have done a task !: \n" + taskToBeDone);
        }
        else {
            taskToBeDone.setDone(false);
            System.out.println("I am unable to do this job..." + taskToBeDone);
        }
        ous.writeObject(taskToBeDone);
        Message message = (Message) ois.readObject();
        System.out.println(message);
        ois.close();
        ous.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ClientApp clientApp = new ClientApp();
        for(int i = 0; i < 4; i++){
            clientApp.simulateIWantAJobDone();
        }
        Thread.sleep(1000);
        for(int i = 0; i < 4; i++){
            clientApp.simulateIWantAJob();
        }
    }
}
