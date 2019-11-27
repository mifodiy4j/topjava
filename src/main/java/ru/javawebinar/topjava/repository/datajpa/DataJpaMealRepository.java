package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository mealRepository;

    @Autowired
    private CrudUserRepository userRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = userRepository.getOne(userId);
        meal.setUser(user);
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return mealRepository.getBetween(startDate, endDate, userId);
    }
}
