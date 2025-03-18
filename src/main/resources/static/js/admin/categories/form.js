$(document).ready(function() {
    $('#parentIdLevel1').val('-');
    $('#parentIdLevel2').val('');
    $('#parentIdLevel3').val('');

    const firstRow = $('.level1-row').first();
    if (firstRow.length) {
        selectCategoryLevel1(firstRow[0]);
    }

    $(document).on('click', '.level1-row', function(event) {
        selectCategoryLevel1(this, event);
    });

    $(document).on('click', '.level2-row', function(event) {
        selectCategoryLevel2(this, event);
    });

    $(document).on('click', '.edit-category-btn', editCategory);
    $(document).on('click', '.delete-category-btn', deleteCategory);

    $('#category-level1 .btn-primary').on('click', function(event) {
        addCategory(1, event);
    });
    $('#category-level2 .btn-primary').on('click', function(event) {
        addCategory(2, event);
    });
    $('#category-level3 .btn-primary').on('click', function(event) {
        addCategory(3, event);
    });
});

let selectedLevel1Id = null;
let selectedLevel2Id = null;

// 카테고리 추가
function addCategory(level) {
    let parentId = '';
    let categoryName = '';

    switch(level) {
        case 1: // 대분류 추가
            parentId = $('#parentIdLevel1').val();
            categoryName = $('#categoryNameLevel1').val();
            break;
        case 2: // 중분류 추가
            parentId = $('#parentIdLevel2').val();
            categoryName = $('#categoryNameLevel2').val();
            break;
        case 3: // 소분류 추가
            parentId = $('#parentIdLevel3').val();
            categoryName = $('#categoryNameLevel3').val();
            break;
    }

    if (!categoryName) {
        alert('카테고리 이름을 입력해주세요.');
        return;
    }

    if (level > 1 && !parentId) {
        alert('상위 카테고리를 먼저 선택해주세요.');
        return;
    }

    const formData = {
        codeId: level+3,
        name: categoryName,
        parentId: parentId === '-' ? null : parentId,
    };

    $.ajax({
        url: "/api/v1/admin/categories/insert",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function (response) {
            const newCategory = response
            const newRow = createCategoryRow(newCategory, level)

            console.log(level)
            console.log(selectedLevel1Id)
            console.log(selectedLevel2Id)
            console.log(newCategory.parentId)
            if (level === 1) {
                $('#category-level1 tbody').append(newRow);
            } else if (level === 2) {
                const parentIdStr = String(selectedLevel1Id);
                const newParentIdStr = String(newCategory.parentId);

                if (parentIdStr === newParentIdStr) {
                    $('#category-level2 tbody').append(newRow);
                    $(`tr.level2-row[data-id="${newCategory.id}"]`).show();
                }
            } else if (level === 3) {
                const parentIdStr = String(selectedLevel2Id);
                const newParentIdStr = String(newCategory.parentId);

                if (parentIdStr === newParentIdStr) {
                    $('#category-level3 tbody').append(newRow);
                    $(`tr.level3-row[data-id="${newCategory.id}"]`).show();
                }
            }
            $(`#categoryNameLevel${level}`).val('');
            alert('카테고리가 추가되었습니다.');
        },
        error: function (e) {
            alert('카테고리 추가에 실패했습니다: ' + e);
        }
    })
}

function createCategoryRow(category, depth) {
    const levelClass = `level${depth}-row`;
    const parentIdDisplay = category.parentId ? category.parentId : '-';
    const rowHtml = `
        <tr class="category-row ${levelClass}" data-id="${category.id}" data-parent-id="${category.parentId}">
            <td>${category.id}</td>
            <td>${category.name} (0)</td>
            <td>${parentIdDisplay}</td>
            <td class="action-buttons">
                <button class="edit-category-btn">✎</button>
                <button class="delete-category-btn">☒</button>
            </td>
        </tr>
    `;
    return rowHtml;
}

// 대분류 선택 처리
function selectCategoryLevel1(row, event) {
    $('.level1-row').removeClass('selected');
    $(row).addClass('selected');

    const categoryId = $(row).attr('data-id');
    selectedLevel1Id = categoryId;
    selectedLevel2Id = null;

    $('#parentIdLevel2').val(selectedLevel1Id);
    $('#parentIdLevel3').val('');

    let visibleLevel2Count = 0;

    $('.level2-row').each(function() {
        if ($(this).attr('data-parent-id') === categoryId) {
            $(this).show();
            visibleLevel2Count++;
            $(this).removeClass('selected');
        } else {
            $(this).hide();
        }
    });

    $('.level3-row').hide().removeClass('selected');
}

// 중분류 선택 처리
function selectCategoryLevel2(row, event) {
    event.stopPropagation();

    $('.level2-row').removeClass('selected');
    $(row).addClass('selected');

    const categoryId = $(row).attr('data-id');
    selectedLevel2Id = categoryId;

    $('#parentIdLevel3').val(categoryId);

    let visibleLevel3Count = 0;

    $('.level3-row').each(function() {
        if ($(this).attr('data-parent-id') === categoryId) {
            $(this).show();
            visibleLevel3Count++;
        } else {
            $(this).hide();
        }
    });
}

// 카테고리 삭제
function deleteCategory(event) {
    event.stopPropagation();

    const row = $(this).closest('tr');
    const categoryId = row.attr("data-id");
    const categoryName = row.find('td:nth-child(2)').text().split(' (')[0];

    if (confirm(`'${categoryName}' 카테고리를 삭제하시겠습니까?`)) {
        $.ajax({
            url: "/api/v1/admin/categories/delete",
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                id: categoryId
            }),
            success: function (response) {
                row.fadeOut(300, function () {
                    const level = row.hasClass('level1-row') ? 1 : row.hasClass('level2-row') ? 2 : 3;

                    if (level === 1) {
                        $('.level2-row, .level3-row').each(function() {
                            if ($(this).attr('data-parent-id') === categoryId) {
                                $(this).hide();
                            }
                        });
                    } else if (level === 2) {
                        $('.level3-row').each(function() {
                            if ($(this).attr('data-parent-id') === categoryId) {
                                $(this).hide();
                            }
                        });
                    }
                    alert('카테고리가 삭제되었습니다.');
                })
            },
            error: function(error) {
                alert('카테고리 삭제에 실패했습니다: ' + error.responseText);
            }
        })
    }
}

// 카테고리 수정
function editCategory(event) {
    event.stopPropagation()

    const row = $(this).closest('tr');
    const categoryId = row.attr("data-id");
    const categoryCell = row.find('td:nth-child(2)');
    const managementCell = row.find('td:last-child');

    // 카테고리 이름에서 괄호 내용(숫자) 제외하기
    const fullCategoryName = categoryCell.text();
    let categoryName = fullCategoryName;
    const countMatch = fullCategoryName.match(/(.+) \((\d+)\)$/);

    if (countMatch) {
        categoryName = countMatch[1];
        const count = countMatch[2];
    }

    // 현재 셀 내용 저장
    const originalHTML = {
        categoryCell: categoryCell.html(),
        managementCell: managementCell.html()
    };

    // 카테고리 이름 셀을 입력 필드로 변경
    categoryCell.html(`<input type="text" class="form-control edit-category-input" value="${categoryName}">`);

    // 관리 셀을 저장/취소 버튼으로 변경
    managementCell.html(`
        <button class="btn btn-success btn-sm save-category-btn">
            <i class="fas fa-check"></i> 저장
        </button>
        <button class="btn btn-secondary btn-sm cancel-edit-btn">
            <i class="fas fa-times"></i> 취소
        </button>
    `);

    // 저장 버튼 클릭 이벤트
    managementCell.find('.save-category-btn').on('click', function() {
        const newCategoryName = categoryCell.find('input').val().trim();

        if (!newCategoryName) {
            alert('카테고리 이름을 입력해주세요.');
            return;
        }

        $.ajax({
            url: "/api/v1/admin/categories/update",
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                id: categoryId,
                name: newCategoryName
            }),
            success: function(response) {
                // 카운트 부분 유지하면서 이름만 업데이트
                let updatedDisplay = newCategoryName;
                if (countMatch) {
                    updatedDisplay = `${newCategoryName} (${countMatch[2]})`;
                }

                // 셀 내용 복원하고 새 이름으로 업데이트
                categoryCell.html(updatedDisplay);
                managementCell.html(originalHTML.managementCell);

                // 이벤트 핸들러 다시 바인딩
                rebindEvents();

                alert('카테고리가 수정되었습니다.');
            },
            error: function(error) {
                alert('카테고리 수정에 실패했습니다: ' + error.responseText);
            }
        });
    });

    // 취소 버튼 클릭 이벤트
    managementCell.find('.cancel-edit-btn').on('click', function() {
        // 원래 셀 내용으로 복원
        categoryCell.html(originalHTML.categoryCell);
        managementCell.html(originalHTML.managementCell);

        // 이벤트 핸들러 다시 바인딩
        rebindEvents();
    });

    // 입력 필드에 포커스
    categoryCell.find('input').focus();
}

function rebindEvents() {
    // 편집 버튼 이벤트 바인딩
    $('.edit-category-btn').off('click').on('click', editCategory);
    // 삭제 버튼 이벤트 바인딩
    $('.delete-category-btn').off('click').on('click', deleteCategory);
}