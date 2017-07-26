
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('studentBuyCourseController', studentBuyCourseController);

    studentBuyCourseController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'courseEntity', 'CourseAtlas','Learner','courseService','Coach','buyCourseService','BuyCourse'];

    function studentBuyCourseController ($timeout, $scope, $stateParams, $uibModalInstance, entity, courseEntity, CourseAtlas,Learner,courseService,Coach,buyCourseService,BuyCourse) {

        var vm = this;

        vm.buyCourse = entity;
        vm.course = courseEntity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.coaches = Coach.query();
        console.log(vm.buyCourse);
        console.log(vm.course);

        vm.buyCourse.course = vm.course;

        //自动获取课程与课时和课程价格
        $scope.CoursesName = vm.course.courseName;
        vm.buyCourse.remainClass = vm.course.totalClassHour;
        //获取课程购买价格
        vm.buyCourse.paymentAccount = vm.course.coursePrices;
        $scope.getPrices = function (price) {
            vm.buyCourse.paymentAccount = price;
        };
        $scope.getVipPrices = function (price) {
            vm.buyCourse.paymentAccount = price;
        };
        //支付方式
        $scope.payMentWayList = [{
            paymentWay:'微信支付'
        },{
            paymentWay:'现金'
        },{
            paymentWay:'支付宝'
        }];
        $scope.getPaymentWay = function () {
            vm.buyCourse.paymentWay = this.payWay.paymentWay;
        };
        //支付方式
        vm.buyCourse.status = '正常';

        //第一个聚焦样式
        $timeout(function (){
            angular.element('.form-group:eq(0)>select').focus();
        });


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            BuyCourse.save(vm.buyCourse, onSaveSuccess, onSaveError);
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



