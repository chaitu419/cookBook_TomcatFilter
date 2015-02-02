$(document).ready(function ()
    {
        for (key in currentCfg)
            {
                $("#currentCfg").append("<input readonly size='35' type='text' value='" + key + "' id='" + key + "'/> ");

                if (currentCfg[key] == true || currentCfg[key] == false)
                    {
                        $("#currentCfg").append("<select style='width:210px; margin-bottom: 7px;' id='" + key + "_value'><option value='true'>Enabled</option><option value='false'>Disabled</option></select><br/>");
                        $("#" + key + "_value").val(currentCfg[key].toString());
                    }
                else
                    {
                        $("#currentCfg").append("<input style='width:210px; margin-bottom: 7px;' type='text' value='" + currentCfg[key] + "' id='" + key + "_value'/><br/>");
                    }
            }

		$("#status").html("Ready!");
    }
);

$(document).on("click", "#saveCfg", function()
    {
        var newConfig;


        alert ("dobe"); return;

        $.ajax(
            {
                type: "POST",
                data: newConfig,
                contentType: "text/plain; charset=UTF-8",
                url : "/radpf/cfg",
                success: function (response)
                    {
                        $("#status").html(response);
                    },
                error: function ()
                    {
                        alert("Error of saving cfg");
                    }
            });
    });