class RightPanel {
    constructor() {
        this.initEventListeners()
    }

    initEventListeners() {
        $(".category_item").each((index, item) => {
            const $this = $(item);
            const img = $(item).find(".arrow_down");

            $this.on("mouseover", (event) => {
                img.attr("src", "/images/keyboard_arrow_up.png");
                $(event.currentTarget).find(".category-list-container").css("display", "block")
            })

            $this.on("mouseout", (event) => {
                img.attr("src", "/images/keyboard_arrow_down.png");
                $(event.currentTarget).find(".category-list-container").css("display", "none")
            })
        });
    }

}

const rightPanelJs = new RightPanel();