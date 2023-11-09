	$(document).ready(function(){

		//1차카테고리 오버
		$("div#category_menu li a").on("mouseover", function(e){
			e.preventDefault();
			//console.log("오버")
			let sel_first_category = $(this);
			let cg_code = $(this).data("cg_code");

			//console.log("1차 카테고리 코드 : " + cg_code);

			let url = '/category/secondCategory/' + cg_code;
			$.getJSON(url, function(category){

				//console.log("카테고리 정보" + category);

				let str ='<ul class="nav">';
					for(let i=0; i<category.length; i++){
						str += '<li class="nav-item justify-content-center">';
						str += '<a class="nav-link active" href="#" data-cg_code="' + category[i].cg_code +'">'+ category[i].cg_name +'</a>';
						str += '</li>';
					}
				str += '</ul>';

				//console.log(str);

				sel_first_category.parent().parent().next().remove();
				sel_first_category.parent().parent().after(str);
			});

		});

	});