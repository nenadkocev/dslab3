package main.java.kocev.nenad.serialization;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    public enum TaskOperator {
        ADDITION,
        DIVISION,
        MULTIPLICATION,
        SUBTRACTION
    }

    private User requestor;

    private double operand1;
    private double operand2;
    private TaskOperator operator;
    private double result;
    private boolean isDone;

    public Task(User requestor, double operand1, double operand2, TaskOperator operator) {
        this.requestor = requestor;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
    }

    public double getOperand1() {
        return operand1;
    }

    public void setOperand1(double operand1) {
        this.operand1 = operand1;
    }

    public double getOperand2() {
        return operand2;
    }

    public void setOperand2(double operand2) {
        this.operand2 = operand2;
    }

    public TaskOperator getOperator() {
        return operator;
    }

    public void setOperator(TaskOperator operator) {
        this.operator = operator;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public User getRequestor() {
        return requestor;
    }

    public void setRequestor(User requestor) {
        this.requestor = requestor;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Double.compare(task.operand1, operand1) == 0 &&
                Double.compare(task.operand2, operand2) == 0 &&
                requestor.equals(task.requestor) &&
                operator == task.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestor, operand1, operand2, operator);
    }

    @Override
    public String toString() {
        return "Task{" +
                "operand1=" + operand1 +
                ", operand2=" + operand2 +
                ", operator=" + operator +
                ", result=" + result +
                ", isDone=" + isDone +
                '}';
    }
}
