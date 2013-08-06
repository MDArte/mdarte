
function Hints(HINTS_ITEMS)
{
	this.HINTS_ITEMS = HINTS_ITEMS;

	this.show = function (key, id)
	{
		if (key != null && key != "" && HINTS_ITEMS[key] != null)
		{
			if (HINTS_ITEMS[key] != null)
			{
				document.getElementById(id).title = HINTS_ITEMS[key];
			}
			else
			{
				document.getElementById(id).title = key;
			}
		}
	}

	this.hide = function ()
	{
		// empty
	}
}
