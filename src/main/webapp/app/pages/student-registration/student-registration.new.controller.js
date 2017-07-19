(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('studentBuyCourseController', studentBuyCourseController);

    studentBuyCourseController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course', 'CourseAtlas','Learner','courseService','Coach','buyCourseService','BuyCourse'];

    function studentBuyCourseController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Course, CourseAtlas,Learner,courseService,Coach,buyCourseService,BuyCourse) {

        var vm = this;

        vm.buyCourse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        //获取支付方式
        buyCourseService.loadPaymentWay(function (data) {
            $scope.payMentWayList = data;
            $scope.getPaymentWay = function () {
                vm.buyCourse.paymentWay = this.payWay.paymentWay;
            }
        });
        //自动获取课程与课时和课程价格
        vm.buyCourse.remainClass = vm.buyCourse.totalClassHour;
        $scope.curName = vm.buyCourse.courseName;
        //获取课程购买价格
        courseService.loadCourseById(vm.buyCourse.id,function (data) {
            $scope.prices = data;
            console.log($scope.prices);
            vm.buyCourse.paymentAccount = $scope.prices.course_prices;
            $scope.getPrices = function (price) {
                vm.buyCourse.paymentAccount = price;
            };
            $scope.getVipPrices = function (price) {
                vm.buyCourse.paymentAccount = price;
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
            $scope.$emit('catpowerserverApp:buyCourseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.buyTime = false;
        vm.datePickerOpenStatus.operateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
