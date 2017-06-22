/**
 *删除排课
 */
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('CourseSchedulingDeleteController',CourseSchedulingDeleteController);

    CourseSchedulingDeleteController.$inject = ['$uibModalInstance', 'entity', 'CourseScheduling'];

    function CourseSchedulingDeleteController($uibModalInstance, entity, CourseScheduling) {
        var vm = this;

        vm.courseScheduling = entity;

        console.log(vm.courseScheduling);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CourseScheduling.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
/**
 * 删除教练排课中是学员的核销记录
 */
(function() {
    'use strict';

    angular
        .module('catpowerserverApp')
        .controller('LearnerChargeDeleteController',LearnerChargeDeleteController);

    LearnerChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'LearnerCharge'];

    function LearnerChargeDeleteController($uibModalInstance, entity, LearnerCharge) {
        var vm = this;

        vm.learnerCharge = entity;
        console.log(vm.learnerCharge.id);
        console.log("******************************************");                                                                                                                                                                                                                                                                                                                                                                                                                                       console.log(vm.learnerCharge);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LearnerCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();


