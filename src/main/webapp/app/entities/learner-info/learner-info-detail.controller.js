(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerInfoDetailController', LearnerInfoDetailController);

    LearnerInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LearnerInfo', 'Learner'];

    function LearnerInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, LearnerInfo, Learner) {
        var vm = this;

        vm.learnerInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:learnerInfoUpdate', function(event, result) {
            vm.learnerInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
