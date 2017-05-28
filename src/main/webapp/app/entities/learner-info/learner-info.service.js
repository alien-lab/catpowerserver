(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('LearnerInfo', LearnerInfo);

    LearnerInfo.$inject = ['$resource', 'DateUtils'];

    function LearnerInfo ($resource, DateUtils) {
        var resourceUrl =  'api/learner-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
