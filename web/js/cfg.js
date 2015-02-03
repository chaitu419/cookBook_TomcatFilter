$(document).ready(function ()
    {
        for (key in currentCfg)
            {
                $("#currentCfg").append("<input readonly type='text' value='" + key + "' class='paramName ctrl'/> ");

                if (currentCfg[key] == true || currentCfg[key] == false)
                    {
                        $("#currentCfg").append("<select class='ctrl' id='" + key + "_value'><option value='true'>Enabled</option><option value='false'>Disabled</option></select><br/>");
                        $("#" + key + "_value").val(currentCfg[key].toString());
                    }
                else
                    {
                        $("#currentCfg").append("<input class='ctrl' type='text' value='" + currentCfg[key] + "' id='" + key + "_value'/><br/>");
                    }
            }

		$("#status").html("Ready!");
    }
);

$(document).on("click", "#saveCfg", function()
    {
		var newConfig = new Object();
		var toBoolean = function(string)
			{
				switch(string.toLowerCase())
					{
						case "true": return true;
						case "false":  return false;
						default: return string;
					}
			}

		$(".paramName").each(function ()
			{
				newConfig[this.value] = toBoolean($("#" + this.value + "_value").val());
			});

		$("#status").html("Saving ...");

        $.ajax(
            {
                type: "POST",
                data: JSON.stringify(newConfig),
                contentType: "text/plain; charset=UTF-8",
                url : "/radpf/cfg",
                success: function (response)
                    {
                        $("#status").html(response);
                    },
                error: function ()
                    {
						$("#status").html("Error of saving cfg");
                    }
            });
    });