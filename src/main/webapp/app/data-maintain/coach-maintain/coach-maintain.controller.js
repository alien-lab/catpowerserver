/**
 * Created by asus on 2017/6/11.
 */
(function () {
    'use strict';

    var app = angular.module('catpowerserverApp');

    app.controller('coachMaintainController',['$scope','Coach',function ($scope,Coach) {
        var vm = this;
        loadAll();

        function loadAll(){
            Coach.query({

            },onSuccess,onError);
            function onSuccess(data) {
                vm.coaches = data;
                console.log(vm.coaches);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }]);

})();
