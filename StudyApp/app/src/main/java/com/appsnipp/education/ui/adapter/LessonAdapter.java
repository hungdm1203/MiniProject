package com.appsnipp.education.ui.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.model.Lesson;
import com.appsnipp.education.ui.model.LessonStatus;
import com.appsnipp.education.ui.utils.FontSizeUtils;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessons;
    private final LessonListener lessonListener;
    private List<LessonStatus> lessonStatuses;
    private final FontSizePrefManager fontSizePrefManager;
    private final Context context;

    public LessonAdapter(Context context, List<Lesson> lessons, LessonListener lessonListener) {
        this.context = context;
        this.lessons = lessons;
        this.lessonListener = lessonListener;
        this.fontSizePrefManager = new FontSizePrefManager(context);
    }

    public void updateLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    public void updateLessonStatuses(List<LessonStatus> statuses) {
        this.lessonStatuses = statuses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        LessonStatus status = null;
        if (lessonStatuses != null) {
            for (LessonStatus s : lessonStatuses) {
                if (s.getLessonId().equals(lesson.getId())) {
                    status = s;
                    break;
                }
            }
        }
        holder.bind(lesson, status, position);
        
        // Apply font sizes
        FontSizeUtils.applyFontSize(holder.textLessonTitle, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(holder.textLessonDescription, fontSizePrefManager.getFontSize());
        FontSizeUtils.applyFontSize(holder.textLessonNumber, fontSizePrefManager.getFontSize());
    }

    @Override
    public int getItemCount() {
        return lessons != null ? lessons.size() : 0;
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLessonTitle;
        private final TextView textLessonDescription;
        private final ImageView imageVideoIndicator;
        private final ImageView imageCompleted;
        private final TextView textLessonNumber;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            textLessonTitle = itemView.findViewById(R.id.textLessonTitle);
            textLessonDescription = itemView.findViewById(R.id.textLessonDescription);
            imageVideoIndicator = itemView.findViewById(R.id.imageVideoIndicator);
            imageCompleted = itemView.findViewById(R.id.imageCompleted);
            textLessonNumber = itemView.findViewById(R.id.textLessonNumber);
        }

        public void bind(Lesson lesson, LessonStatus status, int position) {
            textLessonNumber.setText(String.valueOf(position + 1));
            textLessonTitle.setText(lesson.getTitle());
            String html = lesson.getContent();
            Spanned spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY);
            String plainText = spanned.toString();
            textLessonDescription.setText(plainText.substring(0, Math.min(plainText.length(), 100)) + "...");
            
            // Show video indicator if video URL exists
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                imageVideoIndicator.setVisibility(View.VISIBLE);
            } else {
                imageVideoIndicator.setVisibility(View.GONE);
            }

            // Show completion status
            if (status != null && status.isCompleted()) {
                imageCompleted.setVisibility(View.VISIBLE);
            } else {
                imageCompleted.setVisibility(View.GONE);
            }
            
            // Setup click listeners
            itemView.setOnClickListener(v -> {
                if (lessonListener != null) {
                    lessonListener.onLessonClicked(lesson, position);
                }
            });
        }
    }

    public interface LessonListener {
        void onLessonClicked(Lesson lesson, int position);
    }
} 