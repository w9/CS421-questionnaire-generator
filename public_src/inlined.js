(() => {
    const bootstrappingLoaderText = document.getElementById(
        "bootstrapping-loader-text"
    );
    const decoder = new TextDecoder("utf-8");

    // Only loading longer than 1 second warrants a loading prompt. Otherwise, save the user from the flash of text.
    let longLoading = false;
    setTimeout(() => {
        longLoading = true;
    }, 250);

    const setLoadingText = (text) => {
        if (longLoading) {
            bootstrappingLoaderText.innerHTML = text;
        }
    };

    const humanize = (x) => {
        if (x < 1024) {
            return `${x}B`;
        } else if (x < 1024 * 1024) {
            return `${(x / 1024).toFixed(1)}KB`;
        } else if (x < 1024 * 1024 * 1024) {
            return `${(x / 1024 / 1024).toFixed(1)}MB`;
        } else {
            return `${(x / 1024 / 1024 / 1024).toFixed(1)}GB`;
        }
    };

    const loadScript = async (path) => {
        setLoadingText(`Loading ${path} ...`);

        const response = await fetch(new Request(path));
        const reader = response.body.getReader();
        const contentLengthHeader = response.headers.get("content-length");
        const totalLength =
            typeof contentLengthHeader == "string"
                ? parseInt(contentLengthHeader)
                : null;
        const totalLengthForHuman =
            typeof totalLength === "number" ? humanize(totalLength) : null;
        let totalReadLength = 0;
        const chunks = [];
        while (true) {
            const { done, value } = await reader.read();
            if (done) {
                break;
            } else {
                chunks.push(value);
                totalReadLength += value ? value.length : 0;
                if (typeof totalLengthForHuman == "string") {
                    const percentage = (
                        (totalReadLength / totalLength) *
                        100
                    ).toFixed(2);
                    setLoadingText(
                        `Loading ${path} (${percentage}% of ${totalLengthForHuman})`
                    );
                } else {
                    setLoadingText(
                        `Loading ${path} (${humanize(totalReadLength)})`
                    );
                }
            }
        }

        const responseArray = new Uint8Array(totalReadLength);
        let offset = 0;
        for (const chunk of chunks) {
            responseArray.set(chunk, offset);
            offset += chunk.length;
        }

        setLoadingText(`Loaded ${path}`);
        const jsText = decoder.decode(responseArray);
        eval.call(window, jsText);
    };

    const main = async () => {
        await loadScript("/js/shared.js");
        await loadScript("/js/main.js");
    };

    main();
})();
