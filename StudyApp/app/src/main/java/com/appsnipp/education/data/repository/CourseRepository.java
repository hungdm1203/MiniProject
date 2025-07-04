package com.appsnipp.education.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.appsnipp.education.data.JsonDataRepository;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private static final String TAG = "CourseRepository";
    private static CourseRepository instance;
    private final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();
    private final Context context;

    private CourseRepository(Context context) {
        this.context = context.getApplicationContext();
        loadCoursesFromJson();
    }

    public static CourseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseRepository(context);
        }
        return instance;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Course>> getFiveCourses() {
        return Transformations.map(allCourses, courses -> {
            return courses.subList(0, Math.min(courses.size(), 3));
        });
    }

    public LiveData<Course> getCourseById(String courseId) {
        MutableLiveData<Course> course = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course c : currentCourses) {
                if (c.getId().equals(courseId)) {
                    course.setValue(c);
                    break;
                }
            }
        }
        return course;
    }

    public LiveData<List<Quiz>> getQuizzesByCourseId(String courseId) {
        MutableLiveData<List<Quiz>> quizzes = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course course : currentCourses) {
                if (course.getId().equals(courseId)) {
                    List<Quiz> courseQuizzes = new ArrayList<>();
                    for (Lesson lesson : course.getLessons()) {
                        if (lesson.getQuiz() != null) {
                            courseQuizzes.add(lesson.getQuiz());
                        }
                    }
                    quizzes.setValue(courseQuizzes);
                    break;
                }
            }
        }
        if (quizzes.getValue() == null) {
            quizzes.setValue(new ArrayList<>());
        }
        return quizzes;
    }

    public LiveData<Quiz> getQuizByCourseIdAndLessonId(String courseId, String lessonId) {
        MutableLiveData<Quiz> quiz = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();
        if (currentCourses != null) {
            for (Course course : currentCourses) {
                if (course.getId().equals(courseId)) {
                    for (Lesson lesson : course.getLessons()) {
                        if (lesson.getId().equals(lessonId) && lesson.getQuiz() != null) {
                            quiz.setValue(lesson.getQuiz());
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return quiz;
    }

    private void loadCoursesFromJson() {
        try {
            List<Course> courses = JsonDataRepository.getInstance(context).getAllCourses();
            allCourses.setValue(courses);
        } catch (Exception e) {
            Log.e(TAG, "Error loading courses: " + e.getMessage());
            allCourses.setValue(new ArrayList<>());
        }
    }

    public LiveData<List<Course>> getCoursesByName(String name) {
        MutableLiveData<List<Course>> result = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();

        if (currentCourses != null) {
            List<Course> filtered = new ArrayList<>();

            for (Course course : currentCourses) {
                if (course.getName().toLowerCase().contains(name.toLowerCase())) {
                    filtered.add(course);
                }
            }

            result.setValue(filtered);
        } else {
            result.setValue(new ArrayList<>());
        }

        return result;
    }

    public LiveData<List<Course>> getCoursesByNameAndType(String name, String type) {
        MutableLiveData<List<Course>> result = new MutableLiveData<>();
        List<Course> currentCourses = allCourses.getValue();

        if (currentCourses != null) {
            List<Course> filtered = new ArrayList<>();

            for (Course course : currentCourses) {
                if (type.toLowerCase().equals("all")) {
                    if (course.getName().toLowerCase().contains(name.toLowerCase())) {
                        filtered.add(course);
                    }
                } else {
                    if (course.getName().toLowerCase().contains(name.toLowerCase()) && course.getType().equals(type.toLowerCase())) {
                        filtered.add(course);
                    }
                }
            }

            result.setValue(filtered);
        } else {
            result.setValue(new ArrayList<>());
        }

        return result;
    }

} 
