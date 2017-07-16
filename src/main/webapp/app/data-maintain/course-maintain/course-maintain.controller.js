/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('courseMaintainController',['$scope','Course','learnerCountService',function ($scope,Course,learnerCountService) {
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
    }]);

})();
