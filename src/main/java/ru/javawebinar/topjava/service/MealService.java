package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll(int userId) {
        List<Meal> meals = (List<Meal>) repository.getAll();
        List<Meal> mealsByUser = meals.stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        if (mealsByUser.isEmpty()) {
            throw new NotFoundException("Not found entity for userId = " + userId);
        }
        List<MealTo> mealsToByUser = MealsUtil.getTos(mealsByUser,
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
        return mealsToByUser;
    }

    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            String message = String.format("Entity with id = %d not owned by userId = %d", id, userId);
            throw new NotFoundException(message);
        }
        return meal;
    }

    public Meal save(Meal meal) {
        return repository.save(meal);
    }

    public boolean delete(int id, int userId) {
        get(id, userId);
        return repository.delete(id);
    }
}