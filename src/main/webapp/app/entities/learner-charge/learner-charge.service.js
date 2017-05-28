(function() {
    'use strict';
    angular
        .module('catpowerserverApp')
        .factory('LearnerCharge', LearnerCharge);

    LearnerCharge.$inject = ['$resource', 'DateUtils'];

    function LearnerCharge ($resource, DateUtils) {
        var resourceUrl =  'api/learner-charges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.chargeTime = DateUtils.convertDateTimeFromServer(data.chargeTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
