(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('Coach', Coach);

    Coach.$inject = ['$resource'];

    function Coach ($resource) {
        var resourceUrl =  'api/coaches/:id';

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
