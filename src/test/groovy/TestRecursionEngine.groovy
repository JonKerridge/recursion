import GPP_Library.LocalDetails
import recursion.RecursionEngine
def ld = new LocalDetails()

// start by commenting out all the properties and each assertion
// will fail .  Vary the type to ensure all combinations occur

def re = new RecursionEngine(
    type: 1,
    workMethod1: "m1",
    workMethod2: "m2",
    splitCount: 2,
    maxDepth: 4,
    lDetails: ld
)
re.run()
