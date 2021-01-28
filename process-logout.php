<!-- this destroys my connection and session -->
<?php
session_start();
session_destroy();
?>
<!-- Quick redirect to previous page -->
<script type="text/javascript">
    window.location = "javascript:history.go(-1)";
</script>
