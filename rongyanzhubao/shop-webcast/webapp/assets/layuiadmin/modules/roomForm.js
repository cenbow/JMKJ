var sessionId;
var project;
layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form
  ,element = layui.element;
  
  var ctx = $("#ctx").val();
  
  init();

  exports('roomForm', {})
});

//删除图片
$(document).on('click', '.rc-upload .detele', function() {
	flushImgBox($(this));
});

//添加图片
$(document).on('change', '.imageBox .rc-upload .imageInput', function(event) {
	var that = $(this);
	var reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);
    reader.onloadend = function (e) {
      var url = e.target.result
      flushImgBox(that,url);
    }
});


//刷新图片
function flushImgBox(that,url){
	var parent = that.parents(".imageBox");
	parent.find(".img").remove();
	if(url){
		parent.find(".jia").after(`<i class="img" _data="${url}" style="background-image: url(${url})" ></i>`);
	}
	
	layui.form.render();
}

function refreshImgBox(obj){
	var url = obj.attr("_val");
	if(url){
		obj.parents(".imageBox").find(".img").remove();
		obj.parents(".imageBox").find(".jia").after(`<i class="img" _data="${url}" style="background-image: url(${url})" ></i>`);
	} 
}

function init(){
	var img = $("input[name='image']");
	refreshImgBox(img);
}
















