(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('BuyCourseDialogController', BuyCourseDialogController);

    BuyCourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BuyCourse', 'Learner', 'Course', 'Coach','courseService','buyCourseService','$filter'];

    function BuyCourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BuyCourse, Learner, Course, Coach,courseService,buyCourseService,$filter) {
        var vm = this;

        vm.buyCourse = entity;
        vm.buyCourse
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.learners = Learner.query();
        vm.courses = Course.query();
        vm.coaches = Coach.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>select').focus();
        });

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
        //买课状态
        vm.buyCourse.status = '正常';
        //获取课时
        $scope.getTotalHour = function () {
            console.log(vm.buyCourse.course.courseName);
            var courseName = vm.buyCourse.course.courseName;
            courseService.loadTotalClassHour(courseName,function (data) {
                $scope.totalClassHour = data;
                vm.buyCourse.remainClass =$scope.totalClassHour.total_class_hour;
            });
            //获取课程购买价格
            var id = vm.buyCourse.course.id;
            console.log(id);
            courseService.loadCourseById(id,function (data) {
                $scope.prices = data;
                console.log($scope.prices);
                vm.buyCourse.paymentAccount=$scope.prices.course_prices;
                $scope.getPrices = function (price) {
                    vm.buyCourse.paymentAccount = price;
                };
                $scope.getVipPrices = function (price) {
                    vm.buyCourse.paymentAccount = price;
                }
            });
        };

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

(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');

    app.factory("buyCourseResource",["$resource",function ($resource) {

        var resourceUrl = "api/buy-courses/paymentWay";
        var result = $resource(resourceUrl,{},{
            'getPaymentWay':{method:'GET',isArray:true},
        });
        return result;
    }]);
    app.service("buyCourseService",["buyCourseResource",function (buyCourseResource) {

        this.loadPaymentWay = function (callback) {
            buyCourseResource.getPaymentWay(function (data) {
                if(callback){
                    callback(data,true)
                }
            },function (error) {
                if (callback) {
                    console.log(" buyCourseResource.getPaymentWay()"+error);
                    callback(error, false)
                }
            });
        };
    }]);
})();
