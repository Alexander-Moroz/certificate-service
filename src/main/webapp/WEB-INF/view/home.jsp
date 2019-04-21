<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.4.0.min.js"
            integrity="sha256-BJeo0qm959uMBGb65z40ejJYGSgR7REI4+CW1fNKwOg="
            crossorigin="anonymous"></script>
    <script type="text/javascript">
        function sendFormData() {
            var formData = $('form[name=newTask]').serializeArray();
            var data = {};
            $(formData).each(function (index, obj) {
                data[obj.name] = obj.value;
            });
            data = JSON.stringify(data);

            $.ajax({
               type: "POST",
               url: "/task",
               data: data,
               success: function (html) {
                   alert(html);
               },
               error: function (jqXHR, exception) {
                   alert(jqXHR.responseText);
               },
               dataType: "json",
               contentType: "application/json; charset=utf-8"
            });
        }
    </script>
</head>
<body>
    <p><a href="http://localhost:8082/">H2 WEB CONSOLE</a>(URL <strong>jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false</strong>)</p>
    <p><a href="/task?id=4">GET CERT TASK STATUS</a></p>
    <p><a href="/cert?id=2">GET CERT</a></p>
    <p><a href="/cert?id=-1">GET not exists id DATA</a></p>

    <form name="newTask">
        <input name="firstName" type="text" value="firstName" required/>
        <input name="name" type="text" value="namenamename" required/>
        <input name="middleName" type="text" value="middleName" required/>
        <input name="nationality" type="text" value="RU" required/>
        <input name="email" type="text" value="email@e.mail"/>
        <input name="phoneNumber" type="text" value="11111111111"/>
    </form>
    <button onclick="sendFormData()">send</button>
</body>
</html>