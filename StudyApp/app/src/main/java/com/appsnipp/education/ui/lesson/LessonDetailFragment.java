package com.appsnipp.education.ui.lesson;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Transaction;

import com.appsnipp.education.R;
import com.appsnipp.education.databinding.FragmentLessonDetailBinding;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.model.Course;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.UserProgress;
import com.appsnipp.education.ui.viewmodel.CourseViewModel;
import com.appsnipp.education.ui.viewmodel.LessonStatusViewModel;
import com.appsnipp.education.ui.viewmodel.ProgressViewModel;

import java.util.List;

public class LessonDetailFragment extends BaseFragment {

    private FragmentLessonDetailBinding binding;
    private CourseViewModel courseViewModel;
    private ProgressViewModel progressViewModel;
    private LessonStatusViewModel lessonStatusViewModel;
    private String lessonId;
    private String courseId;
    private Lesson currentLesson;
    private Course currentCourse;
    private UserProgress userProgress;
    private int lessonIndex = 0;
    private boolean isVideoWatched = false;
    private boolean isQuizCompleted = false;
    private WebView videoWebView;
    private View customView; // Lưu view toàn màn hình
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLessonDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khôi phục trạng thái
        if (savedInstanceState != null) {
            isVideoWatched = savedInstanceState.getBoolean("isVideoWatched", false);
            isQuizCompleted = savedInstanceState.getBoolean("isQuizCompleted", false);
            lessonIndex = savedInstanceState.getInt("lessonIndex", 0);
        }

        if (getArguments() != null) {
            lessonId = getArguments().getString("lessonId");
            courseId = getArguments().getString("courseId");
        }

        setupToolbar();
        setupViewModels();
        observeData();
        setupButtonListeners();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isVideoWatched", isVideoWatched);
        outState.putBoolean("isQuizCompleted", isQuizCompleted);
        outState.putInt("lessonIndex", lessonIndex);
    }

    private void setupToolbar() {
        binding.lessonToolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void setupViewModels() {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        progressViewModel = new ViewModelProvider(requireActivity()).get(ProgressViewModel.class);
        lessonStatusViewModel = new ViewModelProvider(requireActivity()).get(LessonStatusViewModel.class);
    }

    private void observeData() {
        if (courseId != null) {
            courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
                if (course != null) {
                    currentCourse = course;
                    findCurrentLesson(course.getLessons());
                }
            });

            progressViewModel.getUserProgressByCourseId(courseId).observe(getViewLifecycleOwner(), progress -> {
                if (progress != null) {
                    userProgress = progress;
                }
            });

            if (lessonId != null) {
                lessonStatusViewModel.getLessonStatus(courseId, lessonId)
                        .observe(getViewLifecycleOwner(), status -> {
                            if (status != null && status.isCompleted()) {
                                isVideoWatched = true;
                                isQuizCompleted = true;
                                binding.buttonCompleteLesson.setEnabled(false);
                                binding.buttonTakeQuiz.setEnabled(false);
                            } else if (status != null && status.getQuizScore() > 0 && !status.isCompleted()) {
                                onQuizCompleted();
                            } else {
                                binding.buttonCompleteLesson.setEnabled(false);
                            }
                        });
            }
        }
    }

    private void findCurrentLesson(List<Lesson> lessons) {
        if (lessonId != null && lessons != null) {
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                if (lesson.getId().equals(lessonId)) {
                    currentLesson = lesson;
                    lessonIndex = i;
                    setupLessonContent(lesson);
                    break;
                }
            }
        }
    }

    private boolean isDarkThemeEnabled() {
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    private void setupLessonContent(Lesson lesson) {
        binding.lessonToolbar.setTitle(lesson.getTitle());
        binding.textLessonTitle.setText(lesson.getTitle());

        // Setup WebView to display content
        WebSettings webSettings = binding.webViewLessonContent.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu cần
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false); // Cho phép zoom nếu cần
        webSettings.setDisplayZoomControls(false); // Ẩn nút zoom

        int bgColorInt = getResources().getColor(R.color.card_background);
        int textColorInt = getResources().getColor(R.color.contentTextColor);

        // Chuyển sang mã HEX để dùng trong HTML
        String bgColor = String.format("#%06X", (0xFFFFFF & bgColorInt));
        String textColor = String.format("#%06X", (0xFFFFFF & textColorInt));

        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body { background-color: " + bgColor + " ; color: " + textColor + " ; font-family: Arial, sans-serif; margin: 20px; text-align:justify;}" +
                "img { max-width: 100%; height: auto; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div>" + lesson.getContent() + "</div>" +
                "</body>" +
                "</html>";

        binding.webViewLessonContent.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);

        // Setup video if available
        if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
            binding.videoWebViewContainer.setVisibility(View.VISIBLE);
            binding.videoViewLesson.setVisibility(View.VISIBLE);
            setupVideoPlayer(lesson.getVideoUrl());
        } else {
            binding.videoWebViewContainer.setVisibility(View.GONE);
            binding.videoViewLesson.setVisibility(View.GONE);
            isVideoWatched = true; // No video means no need to watch
        }
    }

    private void setupVideoPlayer(String videoUrl) {
        binding.videoViewLesson.setVisibility(View.VISIBLE);
        WebSettings webSettings = binding.videoViewLesson.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        videoWebView = binding.videoViewLesson;

        // Thêm interface để giao tiếp từ JavaScript sang Android
        videoWebView.addJavascriptInterface(new WebAppInterface(), "Android");

        videoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Gọi hàm JavaScript để thiết lập video ID
                setVideoId(videoUrl);
            }
        });

        // Tải file HTML từ assets
        videoWebView.loadUrl("file:///android_asset/youtube_player.html");

        // Cấu hình WebChromeClient để hỗ trợ toàn màn hình
        videoWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // Khi vào chế độ toàn màn hình
                if (customView != null) {
                    onHideCustomView();
                    return;
                }
                customView = view;
                customViewCallback = callback;

                // Ẩn WebView và hiển thị customView toàn màn hình
                videoWebView.setVisibility(View.GONE);
                ViewGroup rootView = binding.getRoot();
                rootView.addView(customView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onHideCustomView() {
                // Thoát chế độ toàn màn hình
                if (customView == null) return;

                ViewGroup rootView = binding.getRoot();
                rootView.removeView(customView);
                customView = null;
                customViewCallback.onCustomViewHidden();
                customViewCallback = null;

                // Hiển thị lại WebView
                videoWebView.setVisibility(View.VISIBLE);
            }
        });

    }

    // Interface để nhận thông tin từ JavaScript
    public class WebAppInterface {
        @JavascriptInterface
        public void onVideoStateChange(String state) {
            // Xử lý trạng thái video (nếu cần)
            System.out.println("Video state: " + state);
        }

        @JavascriptInterface
        public void onVideoHalfway() {
            // Xử lý khi video xem được 50%
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "You have watched 50% of the video!", Toast.LENGTH_SHORT).show();
                isVideoWatched = true;
                checkCompletionStatus();
            });
        }

        @JavascriptInterface
        public void onVideoError(int errorCode) {
            requireActivity().runOnUiThread(() -> {
                String errorMessage;
                switch (errorCode) {
                    case 0:
                        errorMessage = "Player is not ready!";
                        break;
                    case 2:
                        errorMessage = "Video ID invalid!";
                        break;
                    case 100:
                        errorMessage = "Video not found!";
                        break;
                    case 101:
                    case 150:
                        errorMessage = "Video embedding is restricted!";
                        break;
                    default:
                        errorMessage = "Unknown video error: " + errorCode;
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                // Lỗi thì nghe nhạc Jack tạm nhé =))
                setVideoId("KYrnTn9nXFI");
                // Hoặc có thể ẩn video
//                binding.videoWebViewContainer.setVisibility(View.GONE);
//                binding.videoViewLesson.setVisibility(View.GONE);
//                isVideoWatched = true; // Đánh dấu video đã "xem" để bỏ qua yêu cầu
//                checkCompletionStatus();
            });
        }
    }

    public void setVideoId(String videoId) {
        // Gọi hàm JavaScript để thiết lập video ID
        videoWebView.evaluateJavascript("loadVideoById('" + videoId + "');", null);
    }

    // Gọi hàm JavaScript từ Android
    public void playVideo() {
        videoWebView.evaluateJavascript("playVideo();", null);
    }

    public void pauseVideo() {
        videoWebView.evaluateJavascript("pauseVideo();", null);
    }

    public void stopVideo() {
        videoWebView.evaluateJavascript("stopVideo();", null);
    }

    private void setupButtonListeners() {
        binding.buttonTakeQuiz.setOnClickListener(v -> {
            // Navigate to quiz fragment
            Bundle args = new Bundle();
            args.putString("lessonId", lessonId);
            args.putString("courseId", courseId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_lessonDetailFragment_to_quizFragment, args);
        });

        binding.buttonCompleteLesson.setOnClickListener(v -> {
            markLessonAsComplete();
        });
    }

    private void checkCompletionStatus() {
        boolean canComplete = isVideoWatched && isQuizCompleted;
        binding.buttonCompleteLesson.setEnabled(canComplete);
    }

    public void onQuizCompleted() {
        isQuizCompleted = true;
        checkCompletionStatus();
    }

    public void updateLastAccessed(String courseId) {
        progressViewModel.updateLastAccess(courseId);
    }

    @Transaction
    private void markLessonAsComplete() {
        if (currentCourse != null && currentLesson != null && userProgress != null) {
            // Update LessonStatus, and auto increment completed lessons in user progress
            lessonStatusViewModel.completeLessonWithoutQuiz(courseId, lessonId);

            // Update UserProgress
            progressViewModel.updateProgress(courseId, currentCourse.getLessonCount(), userProgress.getCompletedLessons() + 1);

            // Hiển thị Toast và điều hướng
            Toast.makeText(requireContext(), getString(R.string.lesson_completed), Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.courseDetailFragment, false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (videoWebView != null) {
            videoWebView.destroy();
        }
    }
} 