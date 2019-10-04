package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
//        List<UserMealWithExceed> result = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        List<UserMealWithExceed> result = getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> result = getFilteredWithExceededStreamHard(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed userMealWithExceed : result) {
            System.out.println(userMealWithExceed);
        }
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDayMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            Integer caloriesSumPerDay = caloriesSumPerDayMap.getOrDefault(localDate, 0) + userMeal.getCalories();
            caloriesSumPerDayMap.put(localDate, caloriesSumPerDay);
        }
        List<UserMealWithExceed> result = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            LocalDateTime dateTime = userMeal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                boolean exceed = caloriesSumPerDayMap.get(dateTime.toLocalDate()) > caloriesPerDay;
                UserMealWithExceed userMealWithExceed = new UserMealWithExceed(dateTime, userMeal.getDescription(), userMeal.getCalories(), exceed);
                result.add(userMealWithExceed);
            }
        }
        return result;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime,
                                                                          LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(u -> u
                .getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        List<UserMealWithExceed> result = mealList.stream().filter(u -> TimeUtil.isBetween(u.getDateTime().toLocalTime(),
                startTime, endTime))
                .map(u -> new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(),
                        caloriesSumByDate.get(u.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
        return result;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededStreamHard(List<UserMeal> mealList,
                                                                              LocalTime startTime, LocalTime endTime,
                                                                              int caloriesPerDay) {
        Collection<List<UserMeal>> listGroupingByDate = mealList.stream().collect(Collectors.groupingBy(
                u -> u.getDateTime().toLocalDate())).values();

        return listGroupingByDate.stream().flatMap(dayMeals -> {
            int caloriesByDay = dayMeals.stream().mapToInt(UserMeal::getCalories).sum();
            boolean exceed = caloriesByDay > caloriesPerDay;
            return dayMeals.stream().filter(meal ->
                    TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .map(meal -> createWithExceed(meal, exceed));
        })
                .collect(Collectors.toList());
    }

    private static UserMealWithExceed createWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                exceed);
    }
}
