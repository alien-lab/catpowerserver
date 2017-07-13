/**
 * Created by zhaoy on 2016/12/13.
 */


(function () {
    'use strict';

    angular.module("app",['aiyun.single.file.upload'])
        .run(run);

    run.$inject = [];

    function run() {

    }

    angular.module("app").controller("appController",AppController);
    AppController.$inject = ["$scope"];
    function AppController($scope) {
        var vm = this;
        vm.isSingle = false;
        vm.url = 'http://localhost:8081/web/fileUpload';
        vm.files = "abc.png";
        vm.getAllFiles = function (fileList) {
            console.log(vm.files);
        }
    }
})();