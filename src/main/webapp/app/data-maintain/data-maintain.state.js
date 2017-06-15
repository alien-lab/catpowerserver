/**
 * Created by asus on 2017/6/11.
 */
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('data-maintain', {
            abstract: true,
            parent: 'app'
        });
    }
})();
