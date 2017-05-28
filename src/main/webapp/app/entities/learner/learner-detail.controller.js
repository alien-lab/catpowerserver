(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerDetailController', LearnerDetailController);

    LearnerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Learner'];

    function LearnerDetailController($scope, $rootScope, $stateParams, previousState, entity, Learner) {
        var vm = this;

        vm.learner = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:learnerUpdate', function(event, result) {
            vm.learner = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
