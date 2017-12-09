(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('WechatGoodsListDialogController', WechatGoodsListDialogController);

    WechatGoodsListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WechatGoodsList', 'WechatShopCard', 'Course'];

    function WechatGoodsListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WechatGoodsList, WechatShopCard, Course) {
        var vm = this;

        vm.wechatGoodsList = entity;
        vm.clear = clear;
        vm.save = save;
        vm.wechatshopcards = WechatShopCard.query();
        vm.courses = Course.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wechatGoodsList.id !== null) {
                WechatGoodsList.update(vm.wechatGoodsList, onSaveSuccess, onSaveError);
            } else {
                WechatGoodsList.save(vm.wechatGoodsList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('catpowerserverApp:wechatGoodsListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
