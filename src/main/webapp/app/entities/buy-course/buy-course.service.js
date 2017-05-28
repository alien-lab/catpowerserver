(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('BuyCourse', BuyCourse);

    BuyCourse.$inject = ['$resource', 'DateUtils'];

    function BuyCourse ($resource, DateUtils) {
        var resourceUrl =  'api/buy-courses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.buyTime = DateUtils.convertDateTimeFromServer(data.buyTime);
                        data.operateTime = DateUtils.convertDateTimeFromServer(data.operateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
