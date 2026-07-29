package todoapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TodoController {

    private final List<Todo> todos = new ArrayList<>(List.of(
            new Todo(1, "Learn Spring Boot", false),
            new Todo(2, "Build first backend API", false),
            new Todo(3, "Push project to GitHub", false)
    ));

    private int nextId = 4;

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todos;
    }

    @PostMapping("/todos")
    public Todo addTodo(@RequestBody TodoRequest request) {
        Todo todo = new Todo(nextId, request.title(), false);
        todos.add(todo);
        nextId++;

        return todo;
    }

    @DeleteMapping("/todos/{id}")
    public String deleteTodo(@PathVariable int id) {
        boolean removed = todos.removeIf(todo -> todo.id() == id);

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }

        return "Todo deleted";
    }

    @PutMapping("/todos/{id}/done")
    public Todo markTodoAsDone(@PathVariable int id) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);

            if (todo.id() == id) {
                Todo updatedTodo = new Todo(todo.id(), todo.title(), true);
                todos.set(i, updatedTodo);
                return updatedTodo;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
    }
}