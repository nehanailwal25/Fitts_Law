package com.example.assignment1.models

data class TrialDto(
        val buttonWidth: Int,
        val buttonHeight: Int,
        val time: Long,
        val distance: Int,
        val trialNumber: Int,
        val partCount: Int,
        val device: String,
        val buttonx: Float,
        val buttony: Float,
        val indexOfDiff: Float,
        var errorCount: Int
)