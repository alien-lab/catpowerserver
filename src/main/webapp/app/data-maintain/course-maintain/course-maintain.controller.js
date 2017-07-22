/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('courseMaintainController',['$scope','Course','courseService',function ($scope,Course,courseService) {
        var vm = this;
        loadAll();
        function loadAll() {
            Course.query({},onSuccess,onError);
            function onSuccess(data) {
                vm.courses = data;
                console.log(vm.courses);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        $scope.searchShow = true;
        $scope.searchHide = true;
        $scope.allSearchShow = true;
        //获取所有的课程
        courseService.loadAllCourse(function (data) {
            $scope.allCourses = data;
        });
        $scope.getallCourse = function () {
            courseService.loadLikeCourses($scope.likeCourseName,function (data) {
                $scope.searchHide = true;
                $scope.LikeCourses = data;
                console.log($scope.LikeCourses);
            });
        };
        //获取所有的课程类型
        courseService.loadAllCourseType(function (data) {
            $scope.allCourseType = data;
            console.log($scope.allCourseType);
            //根据课程的类型获取课程
            $scope.getCourseByType = function (courseType) {
                $scope.searchShow = true;
                $scope.searchHide = false;
                var index = -1;
                angular.forEach($scope.allCourseType,function (item,key) {
                    if(item.course_type == courseType){
                        index = key;
                    }
                });
                if(index != -1){
                    courseService.loadCourse(courseType,function (data) {
                        $scope.courseList = data;
                        console.log($scope.courseList);
                    });
                }
            }
        });
        //根据课程名称模糊查询
        $scope.likeCourseName = '';

        $scope.search = function () {
            $scope.searchShow = !$scope.searchShow;
            $scope.allSearchShow = !$scope.allSearchShow;
            courseService.loadLikeCourses($scope.likeCourseName,function (data) {
                $scope.LikeCourses = data;
                console.log($scope.LikeCourses);
            });
        };
        /**/

    }]);

})();


(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.factory('courseResource',['$resource',function ($resource) {
        var resourceUrl = "api/courses/courseInfo";
        return $resource(resourceUrl,{},{
            'getAllCourse':{method: 'GET',isArray:true},
            'getCourseByType':{url:'api/courses/type',method:'GET',isArray:true},
            'getAllCourseType':{url:'api/courses/courseType',method:'GET',isArray:true},
            'getTotalClassHour':{url:'api/course/courseName',method:'GET'},
            'getLikeCourse':{url:'api/course/like/courseName',method:'GET',isArray:true},
            'getCourseByCourseId':{url:'api/course/courseId',method:'GET'}
        });
    }]);
    app.service('courseService',['courseResource',function (courseResource) {
        //获取所有的课程信息
        this.loadAllCourse = function (callback) {
            courseResource.getAllCourse({},function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                if(callback){
                    console.log("courseTypeResource.getAllCourse" + error);
                    callback(error,false)
                }
            });
        };
        //获取所有的课程类型
        this.loadAllCourseType = function (callback) {
            courseResource.getAllCourseType({},function (data) {
                if (callback){
                    callback(data,true)
                }
            },function (error) {
                if (callback){
                    console.log("courseTypeResource.getAllCourseType()" + error);
                    callback(error,false)
                }
            });
        };
        //根据课程的类型获取课程
        this.loadCourse = function (courseType,callback) {
            courseResource.getCourseByType({
                'courseType':courseType
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("courseTypeResource.getCourseByType()"+error);
                if(callback){
                    callback(error,false)
                }
            });
        };
        //根据课程的名称获取课时
        this.loadTotalClassHour = function (courseName,callback) {
            courseResource.getTotalClassHour({
                'courseName':courseName
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("courseTypeResource.getTotalClassHour()"+error);
                if(callback){
                    callback(error,false)
                }
            });
        }
        //根据课程名称模糊查询
        this.loadLikeCourses = function (courseName,callback) {
            courseResource.getLikeCourse({
                'courseName':courseName
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("courseTypeResource.getTotalClassHour()"+error);
                if(callback){
                    callback(error,false)
                }
            });
        }
        //根据课程ID获取课程
        this.loadCourseById = function (id,callback) {
            courseResource.getCourseByCourseId({
                'id':id
            },function (data) {
                if(callback){
                    callback(data,true);
                }
            },function (error) {
                console.log("courseTypeResource.getTotalClassHour()"+error);
                if(callback){
                    callback(error,false)
                }
            });
        }
    }]);
})();

