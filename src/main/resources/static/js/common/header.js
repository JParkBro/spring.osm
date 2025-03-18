class Header {
    constructor() {
        this.initEventListener();
        this.currentLevel1Item = null;
        this.currentLevel2Item = null;
        this.currentLevel3Item = null;
    }

    initEventListener() {
        const $categoryMenu = $(".menu-item.category")

        $categoryMenu.on("mouseover", (event) => {
            $(event.currentTarget).find(".menu-sub").css("display", "flex");
        });

        $categoryMenu.on("mouseleave", (event)=> {
            const $menuSub = $(event.currentTarget).find(".menu-sub")
            $menuSub.css("display", "none");
            $menuSub.css("width", "172px")
            $menuSub.find(".category-container:nth-child(3)").css("display", "none")
            $menuSub.find(".category-container:nth-child(4)").css("display", "none")

            if (this.currentLevel1Item) {
                this.currentLevel1Item.removeClass("hover-active");
                this.currentLevel1Item.find(".chevron-icon").css("display", "none");
            }
            if (this.currentLevel2Item) {
                this.currentLevel2Item.removeClass("hover-active");
                this.currentLevel2Item.find(".chevron-icon").css("display", "none");
            }
            if (this.currentLevel3Item) {
                this.currentLevel3Item.removeClass("hover-active");
                this.currentLevel3Item.find(".chevron-icon").css("display", "none");
            }
        });

        const $categoryNodes = $("li.category-node");

        $categoryNodes.each((index, item) => {
            const $this = $(item);
            const level = $this.data("level");
            const id = $this.data("id");

            $this.on("mouseover", () => {
                $this.addClass("hover-active");
                const $icon = $this.find(".chevron-icon");
                $icon.css("display", "block");

                if (level === 1) {
                    if (this.currentLevel1Item && this.currentLevel1Item[0] !== $this[0]) {
                        this.currentLevel1Item.removeClass("hover-active");
                        this.currentLevel1Item.find(".chevron-icon").css("display", "none");
                        if (this.currentLevel2Item) {
                            this.currentLevel2Item.removeClass("hover-active");
                            this.currentLevel2Item.find(".chevron-icon").css("display", "none");
                        }
                        if (this.currentLevel3Item) {
                            this.currentLevel3Item.removeClass("hover-active");
                            this.currentLevel3Item.find(".chevron-icon").css("display", "none");
                        }
                    }

                    this.currentLevel1Item = $this;
                    const currentLevel1Id = id;
                    let hasVisibleLevel2Item = false;

                    $(".category-level2 li.category-node").each((index, item) => {
                        const $level2Item = $(item);
                        if ($level2Item.data("parent-id") === currentLevel1Id) {
                            $level2Item.css("display", "flex");
                            hasVisibleLevel2Item = true;
                        } else {
                            $level2Item.css("display", "none");
                        }
                    })

                    if (hasVisibleLevel2Item) {
                        $(".menu-item.category .menu-sub").css("width", "343px")
                        $(".menu-sub .category-container:nth-child(3)").css("display", "block")
                        $(".menu-sub .category-container:nth-child(4)").css("display", "none")
                    } else {
                        $(".menu-item.category .menu-sub").css("width", "172px")
                        $(".menu-sub .category-container:nth-child(3)").css("display", "none")
                        $(".menu-sub .category-container:nth-child(4)").css("display", "none")
                    }

                } else if (level === 2) {
                    if (this.currentLevel2Item && this.currentLevel2Item[0] !== $this[0]) {
                        this.currentLevel2Item.removeClass("hover-active");
                        this.currentLevel2Item.find(".chevron-icon").css("display", "none");
                        if (this.currentLevel3Item) {
                            this.currentLevel3Item.removeClass("hover-active");
                            this.currentLevel3Item.find(".chevron-icon").css("display", "none");
                        }
                    }

                    this.currentLevel2Item = $this;
                    const currentLevel2Id = id;
                    let hasVisibleLevel3Item = false;

                    $(".category-level3 li.category-node").each((index, item) => {
                        const $level3Item = $(item);
                        if ($level3Item.data("parent-id") === currentLevel2Id) {
                            $level3Item.css("display", "flex");
                            hasVisibleLevel3Item = true;
                        } else {
                            $level3Item.css("display", "none");
                        }
                    })

                    if (hasVisibleLevel3Item) {
                        $(".menu-item.category .menu-sub").css("width", "514px")
                        $(".menu-sub .category-container:nth-child(4)").css("display", "block")
                    } else {
                        $(".menu-item.category .menu-sub").css("width", "343px")
                        $(".menu-sub .category-container:nth-child(4)").css("display", "none")
                    }

                } else if (level === 3) {
                    if (this.currentLevel3Item && this.currentLevel3Item[0] !== $this[0]) {
                        this.currentLevel3Item.removeClass("hover-active");
                        this.currentLevel3Item.find(".chevron-icon").css("display", "none");
                    }

                    this.currentLevel3Item = $this;
                }
            })

            $this.on("click", () => {
                if (level === 3) {
                    window.location.href = `/product/${id}`;
                }
            });
        })
    }
}

const headerJs = new Header();