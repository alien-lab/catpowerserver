(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('studentBuyCourseController', studentBuyCourseController);

    studentBuyCourseController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course', 'CourseAtlas','Learner','courseService','Coach','buyCourseService','BuyCourse'];

    function studentBuyCourseController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Course, CourseAtlas,Learner,courseService,Coach,buyCourseService,BuyCourse)
    {
        var vm = this;

        vm.course = entity;
        console.log(vm.course);

        vm.clear = clear;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.courseatlases = CourseAtlas.query();
        vm.coaches = Coach.query();

        console.log(vm.course.id);

        $timeout(function (){
            angular.element('.form-group:eq(0)>select').focus();
        });
        //根据ID获取课程名称
        var className = vm.course.courseName;
        console.log(className);
        vm.course.course = vm.course.courseName;
        //获取课时
        courseService.loadTotalClassHour(vm.course.courseName,function (data) {
            $scope.totalClassHour = data;
            console.log($scope.totalClassHour);
            vm.course.remainClass = $scope.totalClassHour.total_class_hour;
            console.log($scope.totalClassHour.total_class_hour);
        });
        //获取支付方式
        buyCourseService.loadPaymentWay(function (data) {
            $scope.payMentWayList = data;
            $scope.getPaymentWay = function () {
                vm.buyCourse.paymentWay = this.payWay.paymentWay;
                console.log(this.payWay.paymentWay);
            }
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.buyCourse.id !== null) {
                BuyCourse.update(vm.buyCourse, onSaveSuccess, onSaveError);
            } else {
                BuyCourse.save(vm.buyCourse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:courseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
