(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CoachDetailController', CoachDetailController);

    CoachDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Coach'];

    function CoachDetailController($scope, $rootScope, $stateParams, previousState, entity, Coach) {
        var vm = this;

        vm.coach = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('catpowerserverApp:coachUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
