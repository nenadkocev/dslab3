package main.java.kocev.nenad.serialization;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class Application {
    private Map<User, List<Task>> tasks;
    private Semaphore semaphore;
    private Semaphore semaphore2;

    public Application() {
        tasks = new HashMap<>();
        semaphore = new Semaphore(1);
        semaphore2 = new Semaphore(1);
    }

    public void doneTask(Task task){
        List<Task> tasks = this.tasks.get(task.getRequestor());
        Optional<Task> undoneTask = tasks.stream().filter(t -> t.equals(task)).findFirst();
        if(undoneTask.isPresent()){
            Task task1 = undoneTask.get();
            task1 = task;
            System.out.println(task1);
        }
    }

    public Optional<Task> getUndoneTask() throws InterruptedException {
        semaphore2.acquire();
        Optional<Task> first = tasks.values().stream()
                .flatMap(Collection::stream)
                .filter(task -> !task.isDone())
                .findFirst();
        semaphore2.release();
        return first;
    }

    public void insertNewJob(User user, Task task) throws InterruptedException {
        task.setRequestor(user);
        semaphore.acquire();
        tasks.computeIfAbsent(user, user1 -> new ArrayList<>());
        tasks.get(user).add(task);
        semaphore.release();
    }

    public List<Task> getDoneTasksForUser(User user) {
        if(!tasks.containsKey(user))
            return new ArrayList<>();

        return tasks.get(user).stream()
                .filter(Task::isDone)
                .collect(Collectors.toList());
    }
}
