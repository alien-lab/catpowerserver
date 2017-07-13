/**
 * Created by zhaoy on 2016/12/15.
 */
var gulp = require('gulp'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename');

gulp.task('minjs', function() {
    return gulp.src('single-file-upload-directive.js')
        .pipe(gulp.dest('dist'))
        .pipe(rename({suffix: '.min'}))
        .pipe(uglify())
        .pipe(gulp.dest('dist'));
});