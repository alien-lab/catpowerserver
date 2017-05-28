(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('CoachEvaluate', CoachEvaluate);

    CoachEvaluate.$inject = ['$resource'];

    function CoachEvaluate ($resource) {
        var resourceUrl =  'api/coach-evaluates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
