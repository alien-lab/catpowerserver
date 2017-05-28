(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('Learner', Learner);

    Learner.$inject = ['$resource', 'DateUtils'];

    function Learner ($resource, DateUtils) {
        var resourceUrl =  'api/learners/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.registTime = DateUtils.convertDateTimeFromServer(data.registTime);
                        data.firstTotime = DateUtils.convertDateTimeFromServer(data.firstTotime);
                        data.firstBuyclass = DateUtils.convertDateTimeFromServer(data.firstBuyclass);
                        data.recentlySignin = DateUtils.convertDateTimeFromServer(data.recentlySignin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
