document.addEventListener('DOMContentLoaded', function () {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Expandable table rows for Past Meetings
    const expandableRows = document.querySelectorAll('.expandable-row');
    expandableRows.forEach(row => {
        row.addEventListener('click', function () {
            const targetId = this.getAttribute('data-target');
            const targetElement = document.getElementById(targetId);
            if (targetElement) {
                if (targetElement.style.display === 'none' || targetElement.style.display === '') {
                    targetElement.style.display = 'table-row';
                } else {
                    targetElement.style.display = 'none';
                }
            }
        });
    });
});
