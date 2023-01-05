$(document).ready(function () {

	$("#searchBtn").click(function () {
		$.ajax({
			type: "GET",
			url: "http://localhost:8282/searchPlans",
			success: function (result) {
				$("#availablePlans").empty();
				$("#availablePlans").append(`<div style='text-align:center;font-size:20px;font-family:"Trebuchet MS", Helvetica, sans-serif'>Available Plans</div><br>`);
				for (let i = 0; i < result.length; i++) {

					$("#availablePlans").append('<div class="card-black">' +
						`<div class="card-header"><b>${result[i].planName}</b></div><div class="card-body">` +
						'<blockquote class="blockquote mb-0">' +
						`<h6>(Individual Plan)</h6>` +
						`<h6><b>Monthly Premium</b>: $${result[i].monthlyPrice}</h6>` +
						`<h6><b>Deductible</b>: $${result[i].deductible}</h6>` +
						`<h6><b>Max out of Pocket</b>: $${result[i].maxOutOfPocket}</h6>` +
						`<button id="${result[i].planName}Purchase" class="btn btn-primary" style="float: right; margin-left: 15px;">Purchase Plan</button>` +
						`<button id="view_${result[i].id}" class="btn btn-info" style="float: right;">View Benefits</button>` +
						'</blockquote></div></div><br>');

					$(`#view_${result[i].id}`).click(function () {
						$("#benefitTitle").text(result[i].planName + " Plan Benefits");
						$("#benefit_description").text(result[i].description);
						$("#benBody").empty();

						if (result[i].planName === "Bronze") {
							$("#benBody").append(
								"<tr><td>Doctor visit</td><td>Full Price</td><td>No Charge</td></tr>" +
								"<tr><td>Prescription</td><td>Full Price</td><td>No Charge</td></tr>" +
								"<tr><td>X-rays & Imaging</td><td>Full Price</td><td>No Charge</td></tr>" +
								"<tr><td>Ambulance & ER</td><td>Full Price</td><td>No Charge</td></tr>"
							);
						} else if (result[i].planName === "Silver") {
							$("#benBody").append(
								"<tr><td>Doctor visit</td><td>Full Price</td><td>40%</td></tr>" +
								"<tr><td>Prescription</td><td>Full Price</td><td>40%</td></tr>" +
								"<tr><td>X-rays & Imaging</td><td>Full Price</td><td>40%</td></tr>" +
								"<tr><td>Ambulance & ER</td><td>Full Price</td><td>40%</td></tr>"
							);
						} else {
							$("#benBody").append(
								"<tr><td>Doctor visit</td><td>Half Price</td><td>30%</td></tr>" +
								"<tr><td>Prescription</td><td>Half Price</td><td>30%</td></tr>" +
								"<tr><td>X-rays & Imaging</td><td>Half Price</td><td>30%</td></tr>" +
								"<tr><td>Ambulance & ER</td><td>Half Price</td><td>30%</td></tr>"
							);
						}


						$("#benefitModal").modal();
					});

					$(`#${result[i].planName}Purchase`).click(function () {
						$("#modal_planId").val(result[i].planId);
						$("#modal_planName").val(result[i].planName);
						$("#modal_price").val(result[i].monthlyPrice);
						$("#modal_firstName").val($("#firstName").val());
						$("#modal_lastName").val($("#lastName").val());
						$("#modal_dob").val($("#dob").val());
						$("#modal_state").val($("#stateName").val());
						$("#modal_zipCode").val($("#zipCode").val());
						$("#policyModal").modal();
					});
				}
			},
			error: function (error) {
				console.log("ERROR: ", error);
			}
		});
	});


	$('#individualPlan').change(function () {
		if ($('#individualPlan').is(':checked')) {
			$("#addFamBtn").css('visibility', 'hidden');
		}
	});

	$('#familyPlan').change(function () {
		if ($('#familyPlan').is(':checked')) {
			$("#addFamBtn").css('visibility', 'visible');
		}
	});

	$("#addFamBtn").click(function () {
		$("#famBody").empty();
		$("#familyModal").modal();
	});


	$("#famClose").click(function () {
		$('#policyModal').prop('draggable', true);
	});


	$("#addRowBtn").click(function () {
		var rowCount = $('#famBody tr').length;
		$("#famBody").append(
			`<tr><td><select class="form-control" id="${rowCount}_rel"><option>Spouse</option><option>Children</option></select></td>` +
			`<td><input type="text" class="form-control" id="${rowCount}_fn" style="width:120px"/></td>` +
			`<td><input type="text" class="form-control" id="${rowCount}_ln" style="width:120px"/></td>` +
			`<td><select class="form-control" id="${rowCount}_gender"><option>Male</option><option>Female</option></select></td>` +
			`<td><input type="date" class="form-control" id="${rowCount}_dob"/></tr>`);
	});

	var members = [];

	$("#famSubmit").click(function () {
		while (members.length > 0) {
			members.pop();
		}
		$('#famBody  > tr').each(function (index, tr) {
			members.push(
				{
					"relationToProposer": $(`#${index}_rel`).val(),
					"firstName": $(`#${index}_fn`).val(),
					"lastName": $(`#${index}_ln`).val(),
					"gender": $(`#${index}_gender`).val(),
					"dateOfBirth": $(`#${index}_dob`).val()
				}
			);
		});

	})

	$("#makePolicy").click(function () {
		if ($("#policyList").text() === "Claim list") {
			alert("You're the admin");
			$("#policyModal").modal('toggle');
			return;
		}


		var details = {
			"planId": $("#modal_planId").val(),
			"planName": $("#modal_planName").val(),
			"firstName": $("#modal_firstName").val(),
			"lastName": $("#modal_lastName").val(),
			"gender": $("#selectGender").val(),
			"email": $("#modal_email").val(),
			"phoneNo": $("#modal_phoneNum").val(),
			"dob": $("#modal_dob").val(),
			"address": $("#modal_address").val(),
			"state": $("#modal_state").val(),
			"zipCode": $("#modal_zipCode").val(),
			"beneficiaries": members,
			"monthlyPrice": $("#modal_price").val()
		};

		if (!details.firstName || !details.lastName || !details.email
			|| !details.phoneNo || !details.address || !details.state
			|| !details.zipCode) {
			alert("Fill in all required fields");
			return;
		}

		if ($('#familyPlan').is(':checked') && members.length < 1) {
			alert("Enter your family member(s)' profile");
			$("#familyModal").modal();
			return;

		}


		$.ajax({
			type: "POST",
			url: "http://localhost:8282/createPolicy",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(details),
			dataType: "json",
			success: function (result) {
				if (result.success === true) {
					alert("Policy Created");
					$("#policyId").val(result.policyId);
					$("#proposerEmail").val($("#modal_email").val());
					$("#purchase_price").text(result.totalPrice);
					$("#purchase_amount").val(result.totalPrice);
					if (result.totalPrice > $("#modal_price").val()) {
						$("#purchase_planType").val("Family");
					} else {
						$("#purchase_planType").val("Individual");
					}
					$("#purchaseModal").modal();
				} else {
					alert("Purchase Failed");
				}
				$("#policyModal").modal('toggle');
			},
			error: function (error) {
				console.log("ERROR: ", error);
			}
		});
	});

	$("#purchaseButton").click(function () {

		sessionStorage.setItem('policyId', JSON.stringify($("#policyId").val()));
		sessionStorage.setItem('email', JSON.stringify($("#proposerEmail").val()));
		sessionStorage.setItem('planName', JSON.stringify($("#modal_planName").val()));
		sessionStorage.setItem('planType', JSON.stringify($("#purchase_planType").val()));
		sessionStorage.setItem('physicalAddress', JSON.stringify($("#modal_address").val()));
		sessionStorage.setItem('billingAmount', JSON.stringify($("#purchase_amount").val()));
		sessionStorage.setItem('firstName', JSON.stringify($("#modal_firstName").val()));
		sessionStorage.setItem('lastName', JSON.stringify($("#modal_lastName").val()));
		sessionStorage.setItem('members', JSON.stringify(members));

		$("#purchaseModal").modal('toggle');
	});

	// Behavior of the clear button
	$("#clearSearch").click(function () {
		$('#firstName').val('')
		$('#lastName').val('')
		$('#stateName').val('')
		$('#zipCode').val('')
		$('#dob').val('')
		$('#availablePlans').empty()
	});
});