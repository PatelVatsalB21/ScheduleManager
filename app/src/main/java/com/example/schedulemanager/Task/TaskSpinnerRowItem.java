package com.example.schedulemanager.Task;

public class TaskSpinnerRowItem {

    public Integer LottieRes;
    public String Category;

    public TaskSpinnerRowItem(Integer lottieRes, String category) {
        LottieRes = lottieRes;
        Category = category;
    }

    public Integer getLottieRes() {
        return LottieRes;
    }

    public void setLottieRes(Integer lottieRes) {
        LottieRes = lottieRes;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
