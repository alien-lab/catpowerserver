/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('courseMaintainController',['$scope','Course',function ($scope,Course) {
        var vm = this;
        loadAll();
        function loadAll() {
            Course.query({},onSuccess,onError);
            function onSuccess(data) {
                vm.courses = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }]);
    /*静态页面数据*/
    app.service('courseMaintainService',[function () {
        this.loadCourses = function () {
            var courses = [{
                courseId:'1111',
                courseName:'普拉提',
                coursePicture:'http://preview.quanjing.com/bjisub003/bji02420070.jpg',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'健美健身',
                coursePicture:'http://mpic.tiankong.com/039/85b/03985bbc1b3c65fcda78b37a4cca2ddc/640.jpg@360h',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'动感单车',
                coursePicture:'http://www.quanjing.com/imgbuy/east-ep-a71-6381553.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'综合素质',
                coursePicture:'http://www.quanjing.com/imgbuy/bji02420115.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'基础课程训练',
                coursePicture:'http://www.quanjing.com/imgbuy/jt-ai-020223592.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'瑜伽',
                coursePicture:'http://www.quanjing.com/imgbuy/mf822-08122599.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'形体',
                coursePicture:'http://www.quanjing.com/imgbuy/bld232946.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            },{
                courseId:'1111',
                courseName:'搏击操',
                coursePicture:'http://www.quanjing.com/imgbuy/bld232943.html',
                courseDescription:'课程介绍',
                priceCourse:'3000.00',
                vipPriceCourse:'2400.00',
                studentNumber:'80',
                courseOtherInfo:'课程其他信息',
                totalHour:30
            }];
            return courses;
        }
    }])
})();
