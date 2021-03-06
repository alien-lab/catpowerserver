/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';
    var app = angular.module('catpowerserverApp');
    app.controller('courseMaintainDialogController',['$timeout', '$scope', '$uibModalInstance','dataMaintain','Course','courseService',function ($timeout, $scope, $uibModalInstance,dataMaintain,Course,courseService) {
        var vm = this;
        vm.course = dataMaintain;
        vm.uploadurl="./api/image/upload";
        vm.clear = clear;
        vm.save = save;

        //获取所有的课程类型
        courseService.loadAllCourseType(function (data) {
            $scope.allCourseType = data;
            console.log($scope.allCourseType);
            //添加课程类型
            $scope.getcourseType = function (courseType) {
                vm.course.courseType = this.courseType.course_type;
            }
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function save () {
            vm.isSaving = true;
            if (vm.course.id !== null) {
                Course.update(vm.course, onSaveSuccess, onSaveError);
            } else {
                Course.save(vm.course, onSaveSuccess, onSaveError);
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

        //下拉选择是否售卖
        $scope.engineer = {
            name: "Dani",
            currentActivity: "选择是否在线售卖"
        };

        $scope.activities =
            [
                "在线售卖",
                "不在线售卖"
            ];
    }]);
})();
