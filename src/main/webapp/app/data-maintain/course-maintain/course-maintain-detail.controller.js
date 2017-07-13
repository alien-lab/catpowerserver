/**
 * Created by asus on 2017/6/12.
 */
(function () {
    'use strict';
    angular
        .module('catpowerserverApp')
        .controller('courseMaintainDetailController',courseMaintainDetailController);
    courseMaintainDetailController.$inject =  ['$scope', '$rootScope', '$stateParams', 'previousState', 'dataMaintain', 'Course', 'CourseAtlas','learnerCountService'];

    function courseMaintainDetailController($scope, $rootScope, $stateParams, previousState, dataMaintain, Course, CourseAtlas,learnerCountService) {
        var vm = this;
        vm.course = dataMaintain;
        learnerCountService.loadlearnerCounts(vm.course.id,function (data) {
            $scope.learnerCount = data;
            console.log($scope.learnerCount.size);
        });
        vm.previousState = previousState.name;
        var unsubscribe = $rootScope.$on('catpowerserverApp:courseUpdate', function(event, result) {
            vm.course = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }

})();
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    //获取签到二维码
    app.factory("learnerCount",["$resource",function($resource){
        var resourceUrl =  '/api/buy-course/learnerCount/courseId';
        return $resource(resourceUrl, {}, {
            'getLearnerCount': { method: 'GET'}
        });
    }]);
    app.service("learnerCountService",["learnerCount",function (learnerCount) {
        this.loadlearnerCounts = function (courseId,callback) {
            learnerCount.getLearnerCount({
                courseId:courseId
            },function (data) {
                if (callback){
                    callback(data,true);
                }
            },function (error) {
                console.log(" learnerCount.getLearnerCount()" + error)
                if (callback){
                    callback(error,false)
                }

            });
        }
    }]);
})();
